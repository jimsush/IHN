require([
    'jqueryui',
    'underscore',
    'backbone',
    'routers/ihn-router',
    'views/ihn/banner-view',
    'views/ihn/maincontent-view',
    'views/ihn/footer-view',
    '../cookie/IHNCookie'
], function ($, _, Backbone, AppRouter, BannerView, MainContentView, FooterView, IHNCookie) {
  
    var bannerView = null, mainContentView = null, footerView = null;
    var router = null;

    var checkLogin = function() {
    	var isLogin = IHNCookie.readCookie("user");
    	if(typeof isLogin == undefined || isLogin == null) {
    		window.location = 'login.html';
    	}
    };
    
    var initialize = function() {
    	bannerView = new BannerView({
            el: $('#ihn-banner')
        });
      
        mainContentView = new MainContentView({
            el: $('#ihn-maincontent')
        });
      
        footerView = new FooterView({
            el: $('#ihn-footer')
        });
    };
    
    checkLogin();
    initialize();
    
    router = new AppRouter({
      'bannerView':bannerView,
      'mainContentView': mainContentView,
      'footerView': footerView
    });
    
    Backbone.history.start();
  
});