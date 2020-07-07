package com.hpcang

import com.hpcang.base.BaseApplication
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MainApp : BaseApplication(){

    companion object{
        init {
            ClassicsHeader.REFRESH_HEADER_PULLING = "下拉可以刷新"
            ClassicsHeader.REFRESH_HEADER_REFRESHING = "正在刷新..."
            ClassicsHeader.REFRESH_HEADER_LOADING = "正在加载..."
            ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即刷新"
            ClassicsHeader.REFRESH_HEADER_FINISH = "刷新完成"
            ClassicsHeader.REFRESH_HEADER_FAILED = "刷新失败"
            ClassicsHeader.REFRESH_HEADER_SECONDARY = "释放进入二楼"
            ClassicsHeader.REFRESH_HEADER_UPDATE = "上次更新 M-d HH:mm"

            ClassicsFooter.REFRESH_FOOTER_PULLING = "上拉加载更多"
            ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放立即加载"
            ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在刷新..."
            ClassicsFooter.REFRESH_FOOTER_LOADING = "正在加载..."
            ClassicsFooter.REFRESH_FOOTER_FINISH = "加载完成"
            ClassicsFooter.REFRESH_FOOTER_FAILED = "加载失败"
            ClassicsFooter.REFRESH_FOOTER_NOTHING = "-- 仓主，没有更多了 --"
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.colorWhite, R.color.colorBackground) //全局设置主题颜色
                ClassicsHeader(context)
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }
    }

}