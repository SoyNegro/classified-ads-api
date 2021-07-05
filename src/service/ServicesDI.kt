package com.thedarksideofcode

import com.thedarksideofcode.service.ClassifiedService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

fun DI.MainBuilder.bindService() {
bind<ClassifiedService>() with singleton { ClassifiedService() }

}