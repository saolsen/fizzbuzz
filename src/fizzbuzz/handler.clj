(ns fizzbuzz.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as ring]
            [ring.util.io :as io]))

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
      (io/piped-input-stream
        (fn [ostream]
          (doseq [x (range from (inc to))]
            (.write ostream (.getBytes (fizzbuzz-string x)))))))
    (catch NumberFormatException e {:status 400 :body "'from' and 'to' query parameters must exist and be integers."})))

(defroutes app-routes
  (GET "/" {params :params} (fizzbuzz params))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))