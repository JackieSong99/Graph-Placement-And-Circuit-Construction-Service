# Create your views here
# import simplejson
from django.http import HttpResponse
from rest_framework import viewsets, status
from rest_framework.decorators import action
from rest_framework import status
from rest_framework.response import Response
from MyCircuit.circuitcaller import Caller
import json


def hello(request):
    return HttpResponse("MyCircuit hello world")


class CircuitConstructionViewSet(viewsets.ModelViewSet):
    @action(detail=False, methods=['post'])
    def construct(self, request):
        # curl -X POST http://127.0.0.1:8000/circuit-api/circuit-construction/construct/ -H 'Content_Type: application/json' -d '@circuit_input_test6.json'
        print("------------------------------------------")
        print(request.body)
        print(json.loads(request.body))
        print("------------------------------------------")

        with open('circuit_json_data.json', 'w',  encoding='utf-8') as f:
            json.dump(json.loads(request.body), f, ensure_ascii=False,  indent=4)

        constructor = Caller()
        result = constructor.Run()
        print(result)
        return Response(result, status=status.HTTP_200_OK)

    @action(detail=False, methods=['post'])
    def test(self, request):
        # curl -X POST http://127.0.0.1:8000/circuit-api/circuit-construction/test/ -H 'Content_Type: application/json' -d '@circuit_input_test6.json'
        print("**************************************")
        return Response('Circuit Construction test-API Hello', status=status.HTTP_200_OK)

