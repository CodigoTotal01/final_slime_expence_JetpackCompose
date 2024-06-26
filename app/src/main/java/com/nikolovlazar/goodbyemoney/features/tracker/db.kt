package com.nikolovlazar.goodbyemoney.features.tracker

import com.nikolovlazar.goodbyemoney.features.tracker.models.Category
import com.nikolovlazar.goodbyemoney.features.tracker.models.Expense
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

val config = RealmConfiguration.create(schema = setOf(Expense::class, Category::class))
val db: Realm = Realm.open(config)