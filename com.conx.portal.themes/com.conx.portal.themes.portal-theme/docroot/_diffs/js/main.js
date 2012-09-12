function fixLayout() {
	var dockbarHeight = $('#dockbar-wrapper').height();
	$('body').height($(window).height() - dockbarHeight);
	$('body').css('padding-top', dockbarHeight + 'px');
}
$(window).resize(function() {
	fixLayout();
});
jQuery(document).ready(
		function() {
			jQuery(
					"a[@href^='http']:not(a[@href*='"
							+ window.location.host.toLowerCase() + "'])").attr(
					"target", "_blank");
			fixLayout();
		});
bannerNav = {};
(function(n) {
	var m = 0, h = false, l = true, a = null, b = 0.6, f = 12, o = 0.75, i = 1, d, c, j, g, k, e = new Image();
	n(document)
			.ready(
					function() {
						d = n("#banner-nav");
						if (!d.length) {
							return
						}
						c = d.children(".banner");
						j = d.children(".menu");
						g = j.children(".item");
						k = j.children(".playpause");
						m = Math.round(Math.random() * c.length) - 1;
						bannerNav.toBanner = function(q, p) {
							if (h || m === q) {
								return
							} else {
								h = true
							}
							g.eq(m).removeClass("selected");
							g.eq(q).addClass("selected").css("opacity", 0).css(
									"opacity", 1).blur();
							c.eq(q).css("zIndex", 3).animate({
								opacity : 1
							}, p, function() {
								c.eq(m).css({
									opacity : 0,
									zIndex : 1
								});
								c.eq(q).css("zIndex", 2);
								m = q;
								h = false
							})
						};
						bannerNav.setInterval = function() {
							k.removeClass("paused");
							l = false;
							a = setInterval(function() {
								bannerNav.toBanner(m + 1 > c.length - 1 ? 0
										: m + 1, b * 1000)
							}, f * 1000)
						};
						bannerNav.clearInterval = function() {
							k.addClass("paused");
							l = true;
							clearInterval(a)
						};
						bannerNav.playpause = function() {
							if (l) {
								setTimeout(function() {
									bannerNav.next(b * 1000)
								}, 250)
							} else {
								bannerNav.clearInterval()
							}
						};
						bannerNav.prev = function(p) {
							bannerNav.clearInterval();
							bannerNav.toBanner(
									m - 1 < 0 ? c.length - 1 : m - 1, p || 1);
							bannerNav.setInterval()
						};
						bannerNav.next = function(p) {
							bannerNav.clearInterval();
							bannerNav.toBanner(
									m + 1 > c.length - 1 ? 0 : m + 1, p || 1);
							bannerNav.setInterval()
						};
						g.eq(m).addClass("selected");
						j.css("opacity", o);
						e.onload = function() {
							d.bind("mouseenter", function() {
								j.animate({
									opacity : i
								}, 150)
							}).bind("mouseleave", function() {
								j.animate({
									opacity : o
								}, 300)
							});
							c.eq(m).css({
								opacity : 1,
								zIndex : 2
							});
							bannerNav.setInterval()
						};
						if (m < 0) {
							m = 0
						}
						e.src = c.eq(m).css("background-image").replace(
								/^.*url\("{0,}|"{0,}\).*$/g, "")
					})
}(jQuery));
jQuery(document)
		.ready(
				function() {
					jQuery(".tabs-slide")
							.each(
									function() {
										var a = jQuery(this), f = false, b = null, g = a
												.children(".heading"), d = a
												.children(".content"), e = d
												.children(".port"), c = e
												.children(".pan"), i = c
												.children(".list"), h = function(
												j) {
											return parseInt(j, 10) || 0
										};
										d.css("marginLeft", g.width()
												+ h(g.css("borderLeftWidth"))
												+ h(g.css("borderRightWidth"))
												+ h(g.css("paddingLeft"))
												+ h(g.css("paddingRight"))
												+ h(g.css("marginLeft"))
												+ h(g.css("marginRight"))
												+ h(d.css("borderLeftWidth"))
												+ h(d.css("borderRightWidth")));
										d
												.find(".item")
												.bind(
														"mouseover",
														function() {
															if (f) {
																return
															}
															var k = this.className
																	.indexOf("next") > -1 ? -2
																	: 2, j = i
																	.width()
																	- e.width();
															b = setInterval(
																	function() {
																		c
																				.css(
																						"left",
																						Math
																								.min(
																										Math
																												.max(
																														parseInt(
																																c
																																		.css("left"),
																																10)
																																+ k,
																														-j),
																										0))
																	}, 10);
															f = true
														}).bind("mouseout",
														function() {
															if (!f) {
																return
															}
															clearInterval(b);
															f = false
														})
									})
				});
(function(c, b, a) {
	a(document)
			.ready(
					function() {
						var f = (c.addEventListener) ? function(q, r, p) {
							r.addEventListener(q, p, false)
						} : function(q, r, p) {
							r.attachEvent("on" + q, function(s) {
								p.apply(r, [ s ])
							})
						};
						var g = function(q, p) {
							return (new RegExp("(\\s|^)" + p + "\\b"))
									.test(q.className)
						};
						var k = function(q, p) {
							if (!g(q, p)) {
								q.className = q.className + " " + p
							}
						};
						var l = function(q, p) {
							if (g(q, p)) {
								q.className = q.className.replace(new RegExp(
										"[\\s^]" + p + "\\b"), "")
							}
						};
						var n = function(p) {
							var q = {
								left : p.offsetLeft,
								top : p.offsetTop
							};
							while (p = p.offsetParent) {
								q.left += p.offsetLeft;
								q.top += p.offsetTop
							}
							return q
						};
						var o = function() {
							return (new Date()).getTime()
						};
						var e = function(s) {
							var u = s.getElementsByTagName("div")[0], q = true, v = o();
							var p = function() {
								j.appendChild(u);
								u.style.width = Math.max(u.offsetWidth,
										s.offsetWidth)
										+ "px";
								var w = n(s), y = w.left + s.offsetWidth
										- u.offsetWidth + 1, x = w.top
										+ s.offsetHeight;
								u.style.left = y + "px";
								u.style.top = (x - 3) + "px"
							};
							var r = function() {
								u.style.width = null;
								s.appendChild(u)
							};
							var t = function() {
								if (!q) {
									return
								}
								if (d || (o() - v) > 500) {
									l(s, "active");
									r()
								} else {
									c.setTimeout(arguments.callee, 10)
								}
							};
							f("mouseover", s, function(w) {
								d = true;
								q = false;
								if (g(s, "has-menu")) {
									k(s, "active")
								}
								if (u && u.parentNode == s) {
									p()
								}
							});
							f("mouseout", s, function(w) {
								d = false;
								q = true;
								v = o();
								if (u) {
									t()
								} else {
									l(s, "active")
								}
							});
							if (u) {
								f("mouseover", u, function(w) {
									q = false
								});
								f("mouseout", u, function(w) {
									q = true;
									v = o();
									t()
								})
							}
						};
						var i = b.body, j = b.createElement("div"), m = a(
								"#dockbar .dockbar-menu > ul > li").get(), h = -1, d = false;
						i.appendChild(j);
						j.className = "dockbar dockbar-flyout";
						while ((li = m[++h])) {
							e(li)
						}
					})
})(this, document, jQuery);
/*
var cursorOverDockBar = false;
var cursorOverChildMenu = false;
function tryHideDockBar() {
	if (!cursorOverDockBar && !cursorOverChildMenu) {
		$('.child-menu').remove();
		$('#dockbar').animate({ 
			marginTop: '-20px' 
		}, 100);
	}
}
function showDockBar() {
	$('#dockbar').animate({ 
		marginTop: '0px' 
	}, 100);
}
$('#dockbar').mouseenter(function() {
	cursorOverDockBar = true;
	showDockBar();
});
$('#dockbar').mousemove(function() {
	if (!cursorOverDockBar) {
		showDockBar();
	}
});
$('#dockbar').mouseleave(function() {
	cursorOverDockBar = false;
	setInterval(function() { tryHideDockBar(); }, 350);
});
$('.child-menu').mouseenter(function() {
	cursorOverChildMenu = true;
	showDockBar();
});
$('.child-menu').mouseout(function() {
	cursorOverChildMenu = false;
	setInterval(function() { tryHideDockBar(); }, 350);
});
$('.has-menu').mouseenter(function() {
	cursorOverChildMenu = true;
	showDockBar();
});
if ($.browser.msie) {
	$(window).mouseleave(function() {
		cursorOverDockBar = true;
	});
} else {
	$('body').mouseleave(function() {
		cursorOverDockBar = true;
	});
} */