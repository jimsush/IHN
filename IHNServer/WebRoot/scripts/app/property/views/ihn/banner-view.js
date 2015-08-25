define([
  'text!templates/ihn/banner-template.html'
], function (BannerTemplate) {

    var BannerView = Backbone.View.extend({
    	
        template: _.template(BannerTemplate),
        
        initialize: function () {
            this.render();
        },
        
        render: function () {
            this.$el.html(this.template());
            return this;
        }
        
    });

    return BannerView;
    
});