package com.example.moviesapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/*
* This component is attached to the application object's lifecycle
* and provides dependencies to it
* is the parent component of the app
* */
@HiltAndroidApp
class MoviesApplication : Application()