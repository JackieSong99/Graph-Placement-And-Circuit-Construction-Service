from django.urls import re_path as url, include
from rest_framework.routers import DefaultRouter
from . import views

router = DefaultRouter()
router.register(r'optimization', views.OptimizationViewSet, basename='')

urlpatterns = [
    url('^$', views.hello),  # /optimization-api/
    url(r'^', include(router.urls)),  # /optimization-api/optimization/optimize   /optimization-api/optimization/test
]
