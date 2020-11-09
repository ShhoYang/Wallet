package com.highstreet.lib.model

/**
 * @author Yang Shihao
 * @date 2018/11/21
 */
data class ListPaged<T>(var curPage: Int,
                   var pageCount: Int,
                   var size: Int,
                   var total: Int,
                   var datas: ArrayList<T>)