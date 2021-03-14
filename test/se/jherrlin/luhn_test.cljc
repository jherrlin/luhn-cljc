(ns se.jherrlin.luhn-test
  (:require
   [clojure.spec.alpha :as s]
   [clojure.test :as t]
   [clojure.test.check.generators :as gen]
   [se.jherrlin.luhn :as luhn]))


(t/deftest luhns
  (t/is (s/valid? ::luhn/luhn "0112046438"))
  (t/is (s/valid? ::luhn/luhn "967425547947112914710"))
  (t/is (not (s/valid? ::luhn/luhn "1234567890")))

  (t/is (= (luhn/validate "0112046438") "0112046438"))
  (t/is (= (luhn/validate "967425547947112914710") "967425547947112914710"))
  (t/is (nil? (luhn/validate "1234567890")))

  (t/is (= 10 (count (gen/generate (luhn/generator 10)))))
  (t/is (= 15 (count (gen/generate (luhn/generator 15)))))

  (s/def ::swedish-person-nr
    (s/with-gen
      (s/and string? #(= 10 (count %)) luhn/validate)
      (fn [] (luhn/generator 10))))

  (t/is (not (s/valid? ::swedish-person-nr "Hejsan")))
  (t/is (s/valid? ::swedish-person-nr "8909073374"))
  (t/is (= 10 (count (gen/generate (s/gen ::swedish-person-nr)))))
  )
