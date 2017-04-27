var $j = jQuery.noConflict();
            var headerHeight = $j('.header').height();

            $j(window).scroll(function () {
                if ($j(this).scrollTop() > headerHeight) {
                    $j('.nav').addClass('fixed-nav');
                } else {
                    $j('.nav').removeClass('fixed-nav');
                }
            });
 