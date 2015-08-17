require([
    'jquery',
    'underscore',
    'backbone',
    'routers/ihn-router',
    'views/ihn/header-view',
    'views/ihn/sidemenu-view',
    'views/ihn/maincontent-view',
    'views/ihn/footer-view',
    '../cookie/IHNCookie'
], function ($, _, Backbone, AppRouter, HeaderView, SideMenuView, MainContentView, FooterView, IHNCookie) {
  
    var headerView = null, sideMenuView = null, mainContentView = null, footerView = null;
    var router = null;

    var checkLogin = function() {
    	var isLogin = IHNCookie.readCookie("user");
    	if(typeof isLogin == undefined || isLogin == null) {
    		window.location = 'login.html';
    	}
    };
    
    var initialize = function() {
        headerView = new HeaderView({
            el: $('#ihn-header')
        });
      
        sideMenuView = new SideMenuView({
            el: $('#ihn-sidemenu')
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
      'headerView':headerView,
      'sideMenuView': sideMenuView,
      'mainContentView': mainContentView,
      'footerView': footerView
    });
    
    Backbone.history.start();
  
});