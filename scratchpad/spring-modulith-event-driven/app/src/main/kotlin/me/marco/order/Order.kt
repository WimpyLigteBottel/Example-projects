package me.marco.order

data class Order(val id: String, val items: List<String>, val paid: Boolean)
