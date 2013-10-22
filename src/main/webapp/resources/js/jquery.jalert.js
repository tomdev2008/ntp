/*
 * jQuery alert插件
*/
(function($) {
	// 弹出对话框,有回调函数
    $.fn.jalert = function(str,call) {
        var ps = $.extend({
            buttonText: { ok: '确定', cancel: '取消' },
            title: '提示',
            content: str
        });
        var allSel = $('select').hide(), doc = $(document);

        var cache, cacheKey = 'jay_modal';
            $('<div class="modal hide fade" id="jmodal-main" style="display:none;" >\
	            		<div class="modal-header" >\
	    					<strong />\
	    				</div>\
                        <div class="modal-body" id="jmodal-container-content" />\
                        <div class="modal-footer">\
		            		<button class="btn btn-info" />\
		            		<button class="btn"  />\
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
                        call();
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
    
    // 弹出确认框
    $.fn.jalert2 = function(str) {
        var ps = $.extend({
            buttonText:'确定',
            title: '提示',
            content: str
        });
        var allSel = $('select').hide(), doc = $(document);

        var cache, cacheKey = 'jay_confirm';
            $('<div class="modal hide fade" id="jconfirm-main" style="display:none;" >\
                        <div class="modal-header" >\
            				<strong />\
            			</div>\
                        <div class="modal-body" id="jconfirm-container-content" />\
                        <div class="modal-footer">\
		            		<button class="btn btn-info" />\
                        </div>\
                </div>').appendTo('body');
       
        if (window[cacheKey] == undefined) {
            cache = {
                modal: $('#jconfirm-main'),
                body: $('#jconfirm-container-content')
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
            .text(ps.buttonText)
                .unbind('click')
                    .click(function(e) {
                        allSel.show();
                        cache.modal.modal("hide");
         });

        if (typeof ps.content == 'string') {
            $('#jconfirm-container-content').html(ps.content);
        }
        if (typeof ps.content == 'function') {
            ps.content(cache.body);
        }
    };
	 // 弹出确认框,有回调函数
    $.fn.jalert3 = function(str,goOn) {
        var ps = $.extend({
            buttonText:'确定',
            title: '提示',
            content: str
        });
        var allSel = $('select').hide(), doc = $(document);

        var cache, cacheKey = 'jay_confirm';
            $('<div class="modal hide fade" id="jconfirm-main" style="display:none;" >\
                        <div class="modal-header" >\
            				<strong />\
            			</div>\
                        <div class="modal-body" id="jconfirm-container-content" />\
                        <div class="modal-footer">\
		            		<button class="btn btn-info" />\
                        </div>\
                </div>').appendTo('body');
       
        if (window[cacheKey] == undefined) {
            cache = {
                modal: $('#jconfirm-main'),
                body: $('#jconfirm-container-content')
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
            .text(ps.buttonText)
                .unbind('click')
                    .click(function(e) {
                        allSel.show();
                        cache.modal.modal("hide");
						goOn();
         });

        if (typeof ps.content == 'string') {
            $('#jconfirm-container-content').html(ps.content);
        }
        if (typeof ps.content == 'function') {
            ps.content(cache.body);
        }
    }
})(jQuery)