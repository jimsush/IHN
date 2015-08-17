define([
  'text!templates/ihn/maincontent-template.html'
], function (MainContentTemplate) {

  var MainContentView = Backbone.View.extend({
      
	  template: _.template(MainContentTemplate),
	  
	  initialize: function () {
		this.$el.html(this.template());
        this.render();
      },
      
      render: function () {
        return this;
      }
      
  });

  return MainContentView;
});