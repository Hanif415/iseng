package com.hanif.firebasecrud.model

class StuffModel {
    var key: String? = null
    var name: String? = null
    var brand: String? = null
    var price: String? = null

    constructor(stuffName: String?, stuffBrand: String?, stuffPrice: String?) {
        name = stuffName
        brand = stuffBrand
        price = stuffPrice
    }
}