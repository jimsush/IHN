require([
    'jqueryui',
    'underscore',
    'backbone',
    'views/banner-view',
    'views/propertylist-view',
    'views/baidumap-view',
    'views/menu-view',
    'collections/corp',
    '../cookie/IHNCookie',
    'routers/corp-routers'
], function ($, _, Backbone, BannerView, PropertyListView, BaiduMapView, 
		MenuView, PropertyAssetCollection, IHNCookie, CorpRouter) {
	
	var bannerView=null, propertyListView=null; baiduMapView=null;
	var menuView=null;
	var propertyAssetCollection=null;
	var router=null;
	
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
		
		propertyAssetCollection.url='/rest/park/properties?username='+username;
		propertyAssetCollection.fetch({
			  reset : true,
			  success : function(collection, response, options){
				  
			  }, error : function(collection, response, options){
				  alert(response+","+options);
			  }
		});
		
	};
	
	initialize();
	
	router = new CorpRouter({
		'propertyListView': propertyListView,
	});
	Backbone.history.start();
	
});
  