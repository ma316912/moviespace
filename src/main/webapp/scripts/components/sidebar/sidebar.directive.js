angular.module('moviespaceApp')
    .directive('treeViewMenu', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                element.bind('click', function(e) {
                    e.preventDefault();
                    var $this = $(this);
                    var checkElement = $this.next();

                    if ((checkElement.is('.treeview-menu')) && (checkElement.is(':visible'))) {
                        checkElement.slideUp('normal', function () {
                            checkElement.removeClass('menu-open');
                        });
                        checkElement.parent("li").removeClass("active");
                    }
                    else if ((checkElement.is('.treeview-menu')) && (!checkElement.is(':visible'))) {
                        var parent = $this.parents('ul').first();
                        var ul = parent.find('ul:visible').slideUp('normal');
                        ul.removeClass('menu-open');
                        var parent_li = $this.parent("li");
                        checkElement.slideDown('normal', function () {
                            checkElement.addClass('menu-open');
                            parent.find('li.active').removeClass('active');
                            parent_li.addClass('active');
                        });
                    }
                    if (checkElement.is('.treeview-menu')) {
                        e.preventDefault();
                    }
                });
            }
        };
    });