(ns se.jherrlin.test-runner
  (:require [cljs.test :refer-macros [run-tests]]
            [se.jherrlin.luhn-test]))

(run-tests 'se.jherrlin.luhn-test)
