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

(ns ctac-bot.commands
  (:require [clojure.string               :as s]
            [clojure.instant              :as inst]
            [clojure.tools.logging        :as log]
            [java-time                    :as tm]
            [discljord.formatting         :as df]
            [discljord-utils.util         :as u]
            [discljord-utils.message-util :as mu]
            [bot.config                   :as cfg]
            [bot.commands                 :as cmd]))

(defn ^{:bot-command "lookup"} lookup-command!
  "Provides links that look up a person on various internet platforms e.g. !lookup Santa Claus"
  [^String args event-data]
  (if (not (s/blank? args))
    (let [name-qs (java.net.URLEncoder/encode args "UTF-8")]
      (mu/create-message! (:discord-message-channel cfg/config)
                          (:channel-id event-data)
                          :embed (assoc (cmd/embed-template)
                                        :description (str "Here's what the internet has to say about " args ":\n"
                                                          " • [Nextdoor](https://nextdoor.com/search/neighbors/?query=" name-qs ")\n"
                                                          " • [Facebook](https://www.facebook.com/search/people/?q=" name-qs ")\n"
                                                          " • [LinkedIn](https://www.linkedin.com/search/results/people/?keywords=" name-qs ")\n"
                                                          " • [Google](https://www.google.com/search?q=amador+county+" name-qs ")"))))
    (mu/create-message! (:discord-message-channel cfg/config)
                        (:channel-id event-data)
                        :embed (assoc (cmd/embed-template)
                                      :description (str "I need to know who you want to look up. For example: `" cmd/prefix "lookup Santa Claus`")))))

(defn ^{:bot-command "move"} move-command!
  "Moves a conversation to the specified channel e.g. !move #memes"
  [args event-data]
  (when (not (mu/direct-message? event-data))   ; Only respond if the message was sent to a real channel in a server (i.e. not in a DM)
    (let [guild-id                (:guild-id event-data)
          channel-id              (:channel-id event-data)
          discord-message-channel (:discord-message-channel cfg/config)]
      (if (not (s/blank? args))
        (if-let [target-channel-id (second (re-find df/channel-mention args))]
          (if (not= channel-id target-channel-id)
            (let [move-message-id    (:id event-data)
                  _                  (mu/delete-message! discord-message-channel channel-id move-message-id)   ; Don't delete the original message unless we've validated everything
                  target-message-id  (:id (mu/create-message! discord-message-channel
                                                              target-channel-id
                                                              :embed (assoc (cmd/embed-template)
                                                                            :description (str "Continuing the conversation from " (mu/channel-link channel-id) "..."))))
                  target-message-url (mu/message-url guild-id target-channel-id target-message-id)
                  source-message-id  (:id (mu/create-message! discord-message-channel
                                                              channel-id
                                                              :embed (assoc (cmd/embed-template)
                                                                            :description (str "Let's continue this conversation in " (mu/channel-link target-channel-id) " ([link](" target-message-url "))."))))
                  source-message-url (mu/message-url guild-id channel-id source-message-id)]
              (mu/edit-message! discord-message-channel
                                target-channel-id
                                target-message-id
                                :embed (assoc (cmd/embed-template)
                                              :description (str "Continuing the conversation from " (mu/channel-link channel-id)  " ([link](" source-message-url "))..."))))
            (log/info "Cannot move a conversation to the same channel."))
          (log/warn "Could not find target channel in move command."))
        (log/warn "move-command! arguments missing a target channel.")))))

(defn ^{:bot-command "epoch"} epoch-command!
  "Displays the 'epoch seconds' value of the given date (in RFC-3339 format), or now if no value is provided."
  [args event-data]
  (let [channel-id (:channel-id event-data)]
    (try
      (let [d     (if (s/blank? args) (java.util.Date.) (inst/read-instant-date args))
            epoch (long (/ (.getTime ^java.util.Date d) 1000))]
        (mu/create-message! (:discord-message-channel cfg/config)
                            channel-id
                            :embed (assoc (cmd/embed-template) :description (str "`" epoch "`"))))
      (catch RuntimeException re
        (mu/create-message! (:discord-message-channel cfg/config)
                            channel-id
                            :embed (assoc (cmd/embed-template) :description (.getMessage re)))))))

(defn ^{:bot-command "dmath"} dmath-command!
  "Displays the result of the given date math expression e.g. now + 1 day"
  [args event-data]
  (let [channel-id (:channel-id event-data)]
    (try
      (let [[b o v u]  (s/split (s/lower-case (s/trim args)) #"\s+")
            base       (if (= b "now") (.getEpochSecond (tm/instant)) (u/parse-int b))
            op         (case o
                         "-" -
                         "+" +
                         nil)
            val        (u/parse-int v)
            multiplier (case u
                         ("m" "min" "mins" "minutes") 60
                         ("h" "hr" "hrs" "hours")     (* 60 60)
                         ("d" "day" "days")           (* 60 60 24)
                         ("w" "wk" "wks" "weeks")     (* 60 60 24 7)
                         1)]  ; Default to seconds
        (if base
          (if (and op val multiplier)  ; Everything was provided - evaluate the expression
            (mu/create-message! (:discord-message-channel cfg/config)
                                channel-id
                                :embed (assoc (cmd/embed-template) :description (str "`" (op base (* val multiplier)) "`")))
            (if-not (or op val)  ; Only base was provided - display it
              (mu/create-message! (:discord-message-channel cfg/config)
                                  channel-id
                                  :embed (assoc (cmd/embed-template) :description (str "`" base "`")))
              (throw (ex-info "Op, val or multiplier not provided" {}))))
          (throw (ex-info "Base not provided" {}))))
      (catch Exception _
        (mu/create-message! (:discord-message-channel cfg/config)
                            channel-id
                            :embed (assoc (cmd/embed-template) :description (str "Unable to parse date math expression: `" args "`")))))))
