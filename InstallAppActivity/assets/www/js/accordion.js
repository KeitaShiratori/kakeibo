/*	accordion.js
	2013/05/19
	Keita Shiratori
*/

$(function(){
	$(".accordion").simpleAccordion();
});

(function($){
	$.fn.simpleAccordion = function(){
		var accordion = $(".accordion");

		$("> dd:not('dt.active + dd')", accordion).hide();

		$("> dt", accordion).click(function(){
			if($("+ dd", this).css("display") == "none"){

				$(this).addClass("active").next("dd").slideDown("fast");
			}else {
				$(this).removeClass("active").next("dd").slideUp("fast");
			}

		});

		return this;
	}
})(jQuery);


