require([
    'jquery',
    'underscore',
    'backbone',
    'views/banner-view',
    'views/propertylist-view',
    'views/baidumap-view',
    'views/menu-view',
    'collections/corp'
], function ($, _, Backbone, BannerView, PropertyListView, BaiduMapView, MenuView, PropertyAssetCollection) {
	
	var bannerView=null, propertyListView=null; baiduMapView=null;
	var menuView=null;
	var propertyAssetCollection=null;
	
	var initialize = function() {
		propertyAssetCollection=new PropertyAssetCollection();
		
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
		
		propertyAssetCollection.url='/rest/property/properties'; //?username=aaa
		propertyAssetCollection.fetch({
			  success: function(collection, response, options){
				  collection.each(function(propertyAsset){
					  // 
					  // $('#a-ul').add('<li>aaaaa</li>');
					  // propertyListView.add('<li>');
					  //
					  // update baidu map
					  // call another function
					  // 
					  //baiduMapView.loadMap('',null);
				  });
			  },
			  error: function(collection, response, options){
				  alert('error');
			  }
		});
		
	};
	
	initialize();
	
}
  