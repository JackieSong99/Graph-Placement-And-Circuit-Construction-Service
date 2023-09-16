import json
# import simplejson
from django.http import HttpResponse
from rest_framework import viewsets, status
from rest_framework.decorators import action
from rest_framework import status
from rest_framework.response import Response
from MyOptimization.optimizercaller import Caller


def hello(request):
    return HttpResponse("MyOptimization hello world")


class OptimizationViewSet(viewsets.ModelViewSet):
    @action(detail=False, methods=['post'])
    def optimize(self, request):
        # curl -X POST http://127.0.0.1:8000/optimization-api/optimization/optimize/ -H 'Content_Type: application/json' -d '@input_graph.json'
        print("------------------------------------------")
        print(json.loads(request.body))
        print("------------------------------------------")
        with open('optimize_json_data.json', 'w',  encoding='utf-8') as f:
            json.dump(json.loads(request.body), f, ensure_ascii=False,  indent=4)

        # with open('json_data.json', 'w',  encoding='utf-8') as f:
        #     json.dump(request.data, f, ensure_ascii=False, indent=4)
        optimizer = Caller()
        result = optimizer.Run()
        print(result)
        json_object = json.loads(result)
        print(json_object['placement'])
        return Response(result, status=status.HTTP_200_OK)

    @action(detail=False, methods=['post'])
    def test(self, request):
        # curl -X POST http://127.0.0.1:8000/optimization-api/optimization/test/ -H 'Content_Type: application/json' -d '@input_graph.json'
        print("hello")
        return Response('Optimization API Hello', status=status.HTTP_200_OK)
