;
; Copyright © 2021 Peter Monks
;
; Licensed under the Apache License, Version 2.0 (the "License");
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
;
;     http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.
;
; SPDX-License-Identifier: Apache-2.0
;

(ns ctac-bot.config
  (:require [clojure.java.io        :as io]
            [clojure.string         :as s]
            [clojure.edn            :as edn]
            [clojure.core.async     :as async]
            [java-time              :as tm]
            [aero.core              :as a]
            [mount.core             :as mnt :refer [defstate]]
            [discljord.connections  :as dc]
            [discljord.messaging    :as dm]
            [discljord-utils.util   :as u]))

; Because java.util.logging is a hot mess
(org.slf4j.bridge.SLF4JBridgeHandler/removeHandlersForRootLogger)
(org.slf4j.bridge.SLF4JBridgeHandler/install)

; Because Java's default exception behaviour in threads other than main is a hot mess
(Thread/setDefaultUncaughtExceptionHandler
 (reify Thread$UncaughtExceptionHandler
   (uncaughtException [_ t e]
     (u/log-exception e (str "Uncaught exception on " (.getName t))))))

(def boot-time (tm/instant))

; Adds a #split reader macro to aero - see https://github.com/juxt/aero/issues/55
(defmethod a/reader 'split
  [_ _ value]
  (let [[s re] value]
    (when (and s re)
      (s/split s (re-pattern re)))))

(defn validated-config-value
  [m k]
  (let [result (get m k)]
    (if-not (s/blank? result)
      result
      (throw (ex-info (str "Config key '"(name k)"' not provided") {})))))

(declare  config)
(defstate config
          :start (let [raw-config            (if-let [config-file (:config-file (mnt/args))]
                                               (a/read-config config-file)
                                               (a/read-config (io/resource "config.edn")))
                       discord-api-token     (validated-config-value raw-config :discord-api-token)
                       discord-event-channel (async/chan (u/getrn raw-config :discord-event-channel-size 100))]
                   {
                     :discord-event-channel              discord-event-channel
                     :discord-connection-channel         (if-let [connection (dc/connect-bot! discord-api-token
                                                                                              discord-event-channel
                                                                                              :intents #{:guilds :guild-messages :direct-messages})]
                                                           connection
                                                           (throw (ex-info "Failed to connect bot to Discord" {})))
                     :discord-message-channel            (if-let [connection (dm/start-connection! discord-api-token)]
                                                           connection
                                                           (throw (ex-info "Failed to connect to Discord message channel" {})))
                   })
          :stop (async/close!        (:discord-event-channel      config))
                (dc/disconnect-bot!  (:discord-connection-channel config))
                (dm/stop-connection! (:discord-message-channel    config)))


; Note: do NOT use mount for these, since they're used before mount has started
(def ^:private build-info
  (if-let [deploy-info (io/resource "deploy-info.edn")]
    (edn/read-string (slurp deploy-info))
    (throw (RuntimeException. "deploy-info.edn classpath resource not found."))))

(def git-tag
  (s/trim (:tag build-info)))

(def git-url
  (when-not (s/blank? git-tag)
    (str "https://github.com/pmonks/ctac-bot/tree/" git-tag)))

(def built-at
  (tm/instant (:date build-info)))

(defn set-log-level!
  "Sets the log level (which can be a string or a keyword) of the bot, for the given logger aka 'package' (a String, use 'ROOT' for the root logger)."
  [level ^String logger-name]
  (when (and level logger-name)
    (let [logger    ^ch.qos.logback.classic.Logger (org.slf4j.LoggerFactory/getLogger logger-name)                       ; This will always return a Logger object, even if it isn't used
          level-obj                                (ch.qos.logback.classic.Level/toLevel (s/upper-case (name level)))]   ; Note: this code defaults to DEBUG if the given level string isn't valid
      (.setLevel logger level-obj))))

(defn reset-logging!
  "Resets all log levels to their configured defaults."
  []
  (let [lc  ^ch.qos.logback.classic.LoggerContext (org.slf4j.LoggerFactory/getILoggerFactory)
        ci  (ch.qos.logback.classic.util.ContextInitializer. lc)
        url (.findURLOfDefaultConfigurationFile ci true)]
    (.reset lc)
    (.configureByResource ci url)))
