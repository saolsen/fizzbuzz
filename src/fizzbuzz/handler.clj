; TODO JSON response
; TODO response time (HTML response)
(ns fizzbuzz.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as ring]
            [clojure.java.io :as io]))

(defn multiple? [multiple factor]
  (= 0 (mod multiple factor)))

; TODO stream response instead of string (https://groups.google.com/forum/?fromgroups=#!topic/ring-clojure/qIT8D7eQJeQ)
(defn fizzbuzz-printer [from to]
  (let [multiple3? (multiple? from 3)
        multiple5? (multiple? from 5)]
  (cond
  	(< to from) nil
  	(and multiple3? multiple5?) (str "FizzBuzz\n" (fizzbuzz-printer (+ 1 from) to))
  	multiple3? (str "Fizz\n" (fizzbuzz-printer (+ 1 from) to))
  	multiple5? (str "Buzz\n" (fizzbuzz-printer (+ 1 from) to))
  	:else (str from "\n" (fizzbuzz-printer (+ 1 from) to)))))

(defn fizzbuzz [params]
  (try
    (let [from (Integer/parseInt (:from params))
          to (Integer/parseInt (:to params))]
      (fizzbuzz-printer from to))
    (catch NumberFormatException e {:status 400 :body "Invalid Range"}))) 

(defroutes app-routes
  (GET "/" {params :params} (fizzbuzz params))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))