package com.highstreet.wallet.gaojie

import java.math.BigDecimal

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

fun main() {
//    val s = "fiber kind account guide text bundle thought equal pudding section arm certain shoulder check deputy middle floor pudding crime advice shoulder layer choice foster"
//    val list = s.split(" ")
//    for (i in 0..23) {
//        println("${i}---${list[i]}")
//    }
//    for (i in 0..23) {
//        for (j in (i + 1)..23) {
//            if (list[i] == list[j]) {
//                println("${list[i]}---${i}---${j}")
//            }
//        }
//    }
//    println("---------------------------------------------------")

    val s ="11110000000000000011.23"
    println((BigDecimal(s).multiply(BigDecimal(s))).toString())
}