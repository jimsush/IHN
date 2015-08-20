define([
  'text!templates/baidumap-template.html'
], function (BaiduMapTemplate) {

    var BaiduMapView = Backbone.View.extend({
    	
        template: _.template(BaiduMapTemplate),
        
        initialize: function () {
            this.render();
            this.initBaiduMapAPI();
            this.collection.bind("add", this.addOne);
        },
        
        render: function () {
            this.$el.html(this.template());
            return this;
        },
        
        addOne: function(propertyAsset){
        	var marker = new BMap.Marker(new BMap.Point(propertyAsset.get('longitude'), propertyAsset.get('latitude'));
    		map.addOverlay(marker);
    		var label = new BMap.Label(propertyAsset.get('name'),{offset:new BMap.Size(20,-10)});
    		marker.setLabel(label);
    		marker.addEventListener("click", open3dmap); //how to pass parameter
        },
        
        initBaiduMapAPI: function(){
        	var script = document.createElement("script");
        	script.type = "text/javascript";
        	script.src="http://api.map.baidu.com/api?v=1.5&ak=OUPYcShtBuVCQiQBkm3L6rbP";
        	this.$el.appendChild(script);
        },
        
        open3dmap: function open3dmap(){
			window.location = 'index.html';
		},
		
		showlnglat: function showInfo(e){
			
		},

        // how to load baidu map js
        // http://api.map.baidu.com/api?v=1.5&ak=OUPYcShtBuVCQiQBkm3L6rbP
        loadMap: function(divname,propertyAssetList){
    		var map = new BMap.Map(divname); 
    		
    		var firstPropertyAsset=propertyAssetList[0];
    		var point = new BMap.Point(firstPropertyAsset.get('longitude'), firstPropertyAsset.get('latitude'));  
    		map.centerAndZoom(point, 13);                 

    		var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});
    		var top_left_navigation = new BMap.NavigationControl(); 
    		var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}); //右上角，仅包含平移和缩放按钮
    		map.addControl(top_left_control);        
    		map.addControl(top_left_navigation);     
    		map.addControl(top_right_navigation);    
    		
    		var open3dmap=function open3dmap(){
    			window.location = 'index.html';
    		}   
    		
    		propertyAssetList.each(function(propertyAsset){
    			var marker = new BMap.Marker(new BMap.Point(propertyAsset.get('longitude'), propertyAsset.get('latitude'));
        		map.addOverlay(marker);
        		var label = new BMap.Label(propertyAsset.get('name'),{offset:new BMap.Size(20,-10)});
        		marker.setLabel(label);
        		marker.addEventListener("click", open3dmap); //how to pass parameter
    		});
    		
    		var showlnglat=function showInfo(e){
    			//alert(e.point.lng + ", " + e.point.lat);
    		}
    		map.addEventListener("click", showlnglat);
        }
        
    });

    return BaiduMapView;
});