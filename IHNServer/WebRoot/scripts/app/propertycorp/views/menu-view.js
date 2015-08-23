define([
  'text!templates/menu-template.html',
  'jquery'
], function (MenuTemplate,$) {

    var MenuView = Backbone.View.extend({
    	
        template: _.template(MenuTemplate),
        
        initialize: function () {
            this.render();
        },
        
        render: function () {
            this.$el.html(this.template());
            return this;
        }
        
        
        
    });

    return MenuView;
    
});