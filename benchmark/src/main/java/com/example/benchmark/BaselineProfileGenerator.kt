package com.example.benchmark

import androidx.annotation.RequiresApi
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by FPL on 11/04/2025.
 */
@RunWith(AndroidJUnit4::class)
@RequiresApi(28)
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generateBaselineProfile() = baselineProfileRule.collect(
        packageName = "com.example.myapplication",
    ){
        pressHome()
        startActivityAndWait()
    }
}