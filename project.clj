(defproject com.leafclick/pgmig "0.1.0-SNAPSHOT"

  :description "Standalone PostgreSQL Migration Runner"
  :url "https://github.com/leafclick/pgmig"
  :license {:name         "Apache License, Version 2.0"
            :url          "http://www.apache.org/licenses/LICENSE-2.0.html"
            :distribution :repo}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [com.fzakaria/slf4j-timbre "0.3.12" :exclusions [org.clojure/clojure]]
                 [org.slf4j/slf4j-api "1.7.25"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]
                 [org.slf4j/jul-to-slf4j "1.7.25"]
                 [org.slf4j/jcl-over-slf4j "1.7.25"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.clojure/tools.cli "0.4.1"]
                 [mount "0.1.14"]
                 [cprop "0.1.13"]
                 [hikari-cp "1.8.3" :exclusions [com.zaxxer/HikariCP org.slf4j/slf4j-api]]
                 [com.github.leafclick/HikariCP "HikariCP-3.3.2-graal-RC2"]
                 [org.postgresql/postgresql "42.2.5"]
                 [migratus "1.2.2" :exclusions [org.clojure/clojure]]
                 [com.taoensso/tower "3.1.0-beta4"]
                 [com.taoensso/encore "2.102.0"]
                 [trptcolin/versioneer "0.2.0"]]

  :min-lein-version "2.0.0"
  :repositories [["jitpack" "https://jitpack.io"]]

  :jvm-opts ["-Dconf=.lein-env"]
  :source-paths ["src/clj"]
  :test-paths []
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main pgmig.main

  :plugins [[lein-cprop "1.0.3"]]

  :aliases {"ci.test"   ["run" "-m" "circleci.test/dir" :project/test-paths]
            "ci.tests"  ["run" "-m" "circleci.test"]
            "ci.retest" ["run" "-m" "circleci.test.retest"]}

  :profiles
  {:uberjar         {:omit-source  true
                     :aot          :all
                     :global-vars  {*warn-on-reflection* true}
                     :jvm-opts     ["-server" "-Dclojure.compiler.elide-meta=[:doc]" "-Dclojure.compiler.direct-linking=true"]
                     :uberjar-name "pgmig.jar"}

   :docker          [:project/docker :profiles/docker]
   :dev             [:project/dev :profiles/dev]
   :native          [:project/docker :profiles/docker :project/graal]

   :project/docker  {:source-paths   ["env/docker/clj"]
                     :resource-paths ["env/docker/resources"]}
   :project/dev     {:dependencies   [[prone "1.6.1"]
                                      [org.clojure/test.check "0.9.0"]
                                      [circleci/circleci.test "0.4.1"]]
                     :source-paths   ["env/dev/clj"]
                     :test-paths     ["test/clj"]
                     :resource-paths ["env/dev/resources"]
                     :repl-options   {:init-ns user}}

   :project/graal   {:dependencies [[com.github.dblock.waffle/waffle-jna "1.8.1" :exclusions [com.google.guava/guava net.java.dev.jna/jna]]
                                    [net.java.dev.jna/jna-platform "5.1.0"]
                                    [com.ongres.scram/client "1.0.0-beta.2"]]}


   :profiles/dev    {}
   :profiles/docker {}})
