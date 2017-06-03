package zingchart

import zingchart.chartGraphSet.*
import grails.converters.*

class ChartGraphSet {
    
    String type
    def series
    ScaleX scaleX
    ScaleY scaleY

    def ChartGraphSet() {
        throw new Exception("Can only instanciate object with a chart type as the parameter")
    }

    def ChartGraphSet(String type) {
        this.type = type
        this.series = new Series()
        this.scaleX = new ScaleX()
        this.scaleY = new ScaleY()
    }

    def String toJSON() {
        def jsonMap = [:]
        jsonMap.graphset = []
        def graphSetData = [:]
        graphSetData.type = this.type
        graphSetData.'background-color' = '#ffffff'
        graphSetData.chart = ['background-color': '#ffffff']		
        if (this.scaleX.labels != []) {
            graphSetData."scale-x" = [values: this.scaleX.labels]
        }
//        if (this.scaleY.labels != []) {
//            graphSetData."scale-y" = [values: this.scaleY.labels]
//        }
        graphSetData.legend = [
			"draggable" : true,
			"drag-handler" : "icon",
			"minimize":1,
			//"alpha":0.7, //transparency 
			'maxitems' : "6",
			"overflow" : "page"
			]
		
		if (type == 'line') {
			graphSetData.plot = ['line-width' : 1, aspect:"spline"]
			graphSetData.plot.marker = [type : 'circle', size : 2]
		}
		
        graphSetData.series = []
        this.series.seriesValues.each { sv ->
            def seriesData = [:]
            seriesData."values" = sv.values
            if (sv.title) {
                seriesData."text" = sv.title
                seriesData."tooltip-text" = "%k, %t: %v"
            }
            if (sv.animate == true) {
                seriesData.animate = true
                seriesData.effect = sv.effect
            }
            graphSetData.series << seriesData
        }
        jsonMap.graphset << graphSetData
        def js = jsonMap as JSON
        return js.toString()
    }

}
