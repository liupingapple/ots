if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}

/** --- added for OTS --- * */

!function($) {

	$(function() {

		// IE10 viewport hack for Surface/desktop Windows 8 bug
		//
		// See Getting Started docs for more information
		if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
			var msViewportStyle = document.createElement("style");
			msViewportStyle.appendChild(document
					.createTextNode("@-ms-viewport{width:auto!important}"));
			document.getElementsByTagName("head")[0]
					.appendChild(msViewportStyle);
		}

		var $window = $(window)
		var $body = $(document.body)

		var navHeight = $('.navbar').outerHeight(true) + 10

		$body.scrollspy({
			target : '.bs-sidebar',
			offset : navHeight
		})

		$window.on('load', function() {
			$body.scrollspy('refresh')
		})

		$('.bs-docs-container [href=#]').click(function(e) {
			e.preventDefault()
		})

		// back to top
		setTimeout(function() {
			var $sideBar = $('.bs-sidebar')

			$sideBar
					.affix({
						offset : {
							top : function() {
								var offsetTop = $sideBar.offset().top
								var sideBarMargin = parseInt($sideBar.children(
										0).css('margin-top'), 10)
								var navOuterHeight = $('.bs-docs-nav').height()

								return (this.top = offsetTop - navOuterHeight
										- sideBarMargin)
							},
							bottom : function() {
								return (this.bottom = $('.bs-footer')
										.outerHeight(true))
							}
						}
					})
		}, 100)

		setTimeout(function() {
			$('.bs-top').affix()
		}, 100)

		// tooltip demo
		$('.tooltip-demo').tooltip({
			selector : "[data-toggle=tooltip]",
			container : "body"
		})

		$('.tooltip-test').tooltip()
		$('.popover-test').popover()

		$('.bs-docs-navbar').tooltip({
			selector : "a[data-toggle=tooltip]",
			container : ".bs-docs-navbar .nav"
		})

		// popover demo
		$("[data-toggle=popover]").popover()

		// button state demo
		$('#fat-btn').click(function() {
			var btn = $(this)
			btn.button('loading')
			setTimeout(function() {
				btn.button('reset')
			}, 3000)
		})
		
		//------ BEGIN form with bootstrap-datepiker.js -----
		window.prettyPrint && prettyPrint();
		
		// disabling dates
		var nowTemp = new Date(2000,10,10);
		var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(),
				nowTemp.getDate(), 0, 0, 0, 0);

		var beginDate = $('#dpd1').datepicker({
			onRender : function(date) {
				return date.valueOf() < now.valueOf() ? 'disabled' : '';
			}
		}).on('changeDate', function(ev) {
			if (ev.date.valueOf() > endDate.date.valueOf()) {
				var newDate = new Date(ev.date)
				newDate.setDate(newDate.getDate() + 1);
				endDate.setValue(newDate);
			}
			beginDate.hide();
			$('#dpd2')[0].focus();
		}).data('datepicker');
		
		var endDate = $('#dpd2')
				.datepicker(
						{
							//onRender : function(date) {
							//	return date.valueOf() <= beginDate.date
							//			.valueOf() ? 'disabled' : '';
							//}
						}).on('changeDate', function(ev) {
					endDate.hide();
				}).data('datepicker');
				
		//------ END from with bootstrap-datepiker.js -----
		
		//------ BEGIN for container-nav used in pcourse_*.gsp -----
		$(".container-nav a span").each(function(){

		    $(this).click(function(){

			    $("#pick_item").val($(this).attr("id"))
			    $("#topForm").submit();
			});

			if ($(this).attr("id") == $("#pick_item").val()) {
			    $(this).addClass("selected");
			}
		 });
		//------ END for container-nav used in pcourse_*.gsp -----
		
	})

}(jQuery)

