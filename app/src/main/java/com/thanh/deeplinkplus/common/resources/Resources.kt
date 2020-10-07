package com.thanh.deeplinkplus.common.resources

import android.content.Context
import android.content.res.Resources

class Resources {
    companion object{
        private lateinit var context: Context
        fun init(context: Context){
                this.context = context.applicationContext
        }

        fun getContext(): Context{
            return this.context
        }

        fun getResources(): Resources{
            return  context.resources
        }
    }
}