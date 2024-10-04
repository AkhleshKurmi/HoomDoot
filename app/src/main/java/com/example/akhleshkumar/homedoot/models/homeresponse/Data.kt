package com.example.akhleshkumar.homedoot.models.homeresponse

data class Data(
    val coupan_codes: List<Any?>,
    val menus: List<Menu>,
    val product_list: ProductList,
    val product_path: String,
    val slider_path: String,
    val sliders: List<Slider>,
    val sub_categories: List<SubCategory>,
    val sub_category_path: String
)