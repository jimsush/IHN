define([
  'text!templates/ihn/header-template.html'
], function (HeaderTemplate) {

    var HeaderView = Backbone.View.extend({
    	
        template: _.template(HeaderTemplate),
        
        initialize: function () {
            this.render();
        },
        
        render: function () {
            this.$el.html(this.template());
            return this;
        }
        
    });

    return HeaderView;
});