jQuery.fn.preventDoubleClick = function (timeout) {
    $elem = $(this);
    $elem.click(function (e) {
        if ($elem.data('prevent') === true) {
            e.preventDefault();
        } else {
            $elem.data('prevent', true);
            setTimeout(function () { $elem.data('prevent', false); }, timeout);
        }
    });
    return this;
};

$(function () {
    $('a[href]').preventDoubleClick(300);
    $('button').preventDoubleClick(300);
});
