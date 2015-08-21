require([
    'jquery',
    'underscore',
    'backbone',
    'views/banner-view',
    'views/propertylist-view',
    'views/baidumap-view',
    'views/menu-view',
    'collections/corp',
    '../cookie/IHNCookie'
], function ($, _, Backbone, BannerView, PropertyListView, BaiduMapView, 
		MenuView, PropertyAssetCollection, IHNCookie) {
	
	var bannerView=null, propertyListView=null; baiduMapView=null;
	var menuView=null;
	var propertyAssetCollection=null;
	
	var initialize = function() {
		propertyAssetCollection=new PropertyAssetCollection
		
		bannerView = new BannerView({
            el: $('#banner-div')
        });
		propertyListView = new PropertyListView({
            el: $('#propertylist-div'),
            collection: propertyAssetCollection
        });
		baiduMapView = new BaiduMapView({
            el: $('#baidumap-div'),
            collection: propertyAssetCollection
        });
		menuView = new MenuView({
            el: $('#menu-div')
        });
		
		var username=IHNCookie.readCookie("user");
		
		propertyAssetCollection.url='/rest/park/property?username='+username;
		propertyAssetCollection.fetch({
			  reset : true,
			  success : function(collection, response, options){
				  /*collection.each(function(propertyAsset){
					  alert("asset: "+propertyAsset.get('id')+" "
							  +propertyAsset.get('name')+" "
							  +propertyAsset.get('longitude')+" "
							  +propertyAsset.get('latitude'));
				  });*/
			  },
			  error : function(collection, response, options){
				  alert(response+","+options);
			  }
		});
		
	};
	
	initialize();
	
});
  