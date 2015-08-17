define([
  'text!templates/ihn/header-template.html',
  '../../../cookie/IHNCookie'
], function (HeaderTemplate, IHNCookie) {

    var HeaderView = Backbone.View.extend({
    	
        template: _.template(HeaderTemplate),
        
        initialize: function () {
            this.render();
        },
        
        render: function () {
            this.$el.html(this.template());
            return this;
        },
        
        events: {
        	'click .logout': 'logout'
        },
        
        logout: function() {
        	IHNCookie.eraseCookie('user');
        	window.location = 'login.html';
        }
        
    });

    return HeaderView;
});