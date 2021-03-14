(ns se.jherrlin.luhn
  "Validate and generate luhn strings."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [clojure.test.check.generators :as gen]))


(defn validate
  "If `s` is a valid luhn string return it, else nil."
  ([s]
   (let [parse-int #?(:clj  #(Character/digit % 10)
                      :cljs #(js/parseInt %))]
     (when
         (and (string? s)
              (some->> s
                       (re-find #"^\d*$")
                       (reverse)
                       (rest)
                       (map #(parse-int %))
                       (map #(* %1 %2) (cycle [2 1]))
                       (map #(+ (quot % 10) (mod % 10)))
                       (reduce +)
                       (+ (parse-int (last s)))
                       (str)
                       (last)
                       (= \0)))
       s))))

(defn checksum
  "Returns the checksum char of a luhn string, or nil."
  [x]
  (let [parse-int #?(:clj  #(Character/digit % 10)
                     :cljs #(js/parseInt %))]
    (when (string? x)
      (some->> x
               (re-find #"^\d*$")
               (seq)
               (map #(parse-int %))
               (reverse)
               (map #(* %1 %2) (cycle [2 1]))
               (map #(+ (quot % 10) (mod % 10)))
               (reduce +)
               (* 9)
               (str)
               (last)))))

(defn generator
  "Luhn generator that returns a `n` long luhn string."
  [n]
  (gen/bind (gen/vector (gen/choose 0 9) (dec n))
            #(let [seed (str/join %)]
               (gen/return (str seed (checksum seed))))))

(s/def ::luhn
  (s/with-gen
    (s/nilable (s/and string? validate))
    (fn []
      (generator 10))))
