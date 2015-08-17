define([
  'text!templates/ihn/footer-template.html'
], function (FooterTemplate) {

    var FooterView = Backbone.View.extend({
    	
        template: _.template(FooterTemplate),
        
        initialize: function () {
            this.render();
        },
        
        render: function () {
            this.$el.html(this.template());
            return this;
        }
        
    });

    return FooterView;
});