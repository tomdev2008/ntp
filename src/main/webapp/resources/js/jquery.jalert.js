/*
 * jQuery alert插件
*/
(function($) {
	// 弹出对话框,回调函数 可选
    //option: string or {}
    $.fn.jalert = function(option,call) {
        var ps = $.extend({
            buttonText: { ok: '确定', cancel: '关闭' },
            title: '提示',
            content: typeof option == "string" ? option : ""
        },option || {});
        var allSel = $('select').hide();

        var cache, cacheKey = 'jay_modal';
            $('<div class="modal hide fade" id="jmodal-main" >\
	            		<div class="modal-header" >\
	    					<strong />\
	    				</div>\
                        <div class="modal-body" id="jmodal-container-content" />\
                        <div class="modal-footer">\
		            		<button class="btn btn-primary btn-large" />\
		            		<button class="btn btn-large btn-link"  />\
                        </div>\
                </div>').appendTo('body');

        if (window[cacheKey] == undefined) {
            cache = {
                modal: $('#jmodal-main'),
                body: $('#jmodal-container-content')
            };
            cache.title = cache.body.prev().children();
            cache.buttons = cache.body.next().children();
            window[cacheKey] = cache;
        }
        cache = window[cacheKey];

        cache.modal.modal("show");
        cache.title.html(ps.title);
        //OK BUTTON
        cache.buttons.eq(0)
            .text(ps.buttonText.ok)
                .unbind('click')
                    .click(function(e) {
                        allSel.show();
                        cache.modal.modal("hide");
                        typeof call == "function" && call();
                    })
        //CANCEL BUTTON
            .next()
                .text(ps.buttonText.cancel)
                    .one('click', function() { cache.modal.modal("hide"); allSel.show(); });

        if (typeof ps.content == 'string') {
            $('#jmodal-container-content').html(ps.content);
        }
        if (typeof ps.content == 'function') {
            ps.content(cache.body);
        }
    };
})(jQuery)