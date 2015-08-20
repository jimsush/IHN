define([
], function () {

    var BaiduMapView = Backbone.View.extend({
    	map: null,
    	
        initialize: function () {
            this.render();
            this.initBaiduMapAPI();
            this.collection.bind("add", this.addOne);
        },
        
        render: function () {
            return this;
        },
        
        addOne: function(propertyAsset){
        	if(this.collection.size()==1){
        		var point = new BMap.Point(propertyAsset.get('longitude'), propertyAsset.get('latitude'));  
        		this.map.centerAndZoom(point, 13);                 

        		var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});
        		var top_left_navigation = new BMap.NavigationControl(); 
        		var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}); 
        		this.map.addControl(top_left_control);        
        		this.map.addControl(top_left_navigation);     
        		this.map.addControl(top_right_navigation);
        	}
        	
        	var marker = new BMap.Marker(new BMap.Point(propertyAsset.get('longitude'), propertyAsset.get('latitude'));
    		this.map.addOverlay(marker);
    		var label = new BMap.Label(propertyAsset.get('name'),
    				{offset:new BMap.Size(20,-10)});
    		marker.setLabel(label);
    		marker.addEventListener("click", this.open3dmap); //how to pass parameter
        },
        
        initBaiduMapAPI: function(){
        	var script = document.createElement("script");
        	script.type = "text/javascript";
        	script.src="http://api.map.baidu.com/api?v=1.5&ak=OUPYcShtBuVCQiQBkm3L6rbP";
        	this.$el.appendChild(script);
        	
        	this.map = new BMap.Map('baidumap-div'); 
        	this.map.addEventListener("click", this.showlnglat);
        },
        
        open3dmap: function open3dmap(){
			window.location = 'index.html';
		},
		
		showlnglat: function showInfo(e){
			
		}
                
    });

    return BaiduMapView;
    
});