$.noty.layouts.skysail = {
    name     : 'skysail',
    options  : {},
    container: {
        object  : '<ul id="noty_skysail_layout_container" />',
        selector: 'ul#noty_skysail_layout_container',
        style   : function() {
            $(this).css({
                top          : 40,
                right        : 3,
                position     : 'fixed',
                width        : '310px',
                height       : 'auto',
                margin       : 0,
                padding      : 0,
                listStyleType: 'none',
                zIndex       : 10000000
            });
        }
    },
    parent   : {
        object  : '<li />',
        selector: 'li',
        css     : {}
    },
    css      : {
        display: 'none',
        width  : '310px'
    },
    addClass : ''
};

$.noty.defaults = {
  layout: 'skysail',
  theme: 'relax', // or relax
  type: 'alert', // success, error, warning, information, notification
  text: '', // [string|html] can be HTML or STRING

  dismissQueue: true, // [boolean] If you want to use queue feature set this true
  force: false, // [boolean] adds notification to the beginning of queue when set to true
  maxVisible: 5, // [integer] you can set max visible notification count for dismissQueue true option,

  template: '<div class="noty_message"><span class="noty_text"></span><div class="noty_close"></div></div>',

  timeout: 2000, // [integer|boolean] delay for closing event in milliseconds. Set false for sticky notifications
  progressBar: false, // [boolean] - displays a progress bar

  animation: {
    open: {height: 'toggle'}, // or Animate.css class names like: 'animated bounceInLeft'
    close: {height: 'toggle'}, // or Animate.css class names like: 'animated bounceOutLeft'
    easing: 'swing',
    speed: 500 // opening & closing animation speed
  },
  closeWith: ['hover'], // ['click', 'button', 'hover', 'backdrop'] // backdrop click will close all notifications

  modal: false, // [boolean] if true adds an overlay
  killer: false, // [boolean] if true closes all notifications and shows itself

  callback: {
    onShow: function() {},
    afterShow: function() {},
    onClose: function() {},
    afterClose: function() {},
    onCloseClick: function() {},
  },

  buttons: false // [boolean|array] an array of buttons, for creating confirmation dialogs.
};