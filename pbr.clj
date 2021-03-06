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

(def lib       'org.github.pmonks/ctac-bot)
(def version   (format "1.0.%s" (.format (java.text.SimpleDateFormat. "yyyyMMdd") (java.util.Date.))))
(def uber-file "./target/ctac-bot-standalone.jar")
(def main      'bot.main)

(defn set-opts
  [opts]
  (assoc opts
         :lib              lib
         :version          version
         :uber-file        uber-file
         :main             main
         :deploy-info-file "./resources/build-info.edn"
         :write-pom        true
         :pom {:description      "A special purpose Discord bot."
               :url              "https://github.com/pmonks/ctac-bot"
               :licenses         [:license   {:name "Apache License 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}]
               :developers       [:developer {:id "pmonks" :name "Peter Monks" :email "pmonks+ctac@gmail.com"}]
               :scm              {:url "https://github.com/pmonks/ctac-bot" :connection "scm:git:git://github.com/pmonks/ctac-bot.git" :developer-connection "scm:git:ssh://git@github.com/pmonks/ctac-bot.git"}
               :issue-management {:system "github" :url "https://github.com/pmonks/ctac-bot/issues"}}))
