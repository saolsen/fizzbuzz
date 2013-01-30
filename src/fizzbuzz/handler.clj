; TODO JSON response
; TODO response time (HTML response)
(ns fizzbuzz.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as ring]))

(defn multiple? [multiple factor]
  (= 0 (mod multiple factor)))

(defn fizzbuzz-string [x]
  (let [multiple3? (multiple? x 3)
        multiple5? (multiple? x 5)]
  (cond
  	(and multiple3? multiple5?) "FizzBuzz\n"
  	multiple3? "Fizz\n"
  	multiple5? "Buzz\n"
  	:else (str x "\n"))))

(defn fizzbuzz [params]
  (try
    (let [from (Integer/parseInt (:from params))
          to (Integer/parseInt (:to params))]
      (apply str
	      (for [x (range from to)
	            :let [result (fizzbuzz-string x)]]
	        result)))
    (catch NumberFormatException e {:status 400 :body "Invalid Range"})))

(defroutes app-routes
  (GET "/" {params :params} (fizzbuzz params))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))