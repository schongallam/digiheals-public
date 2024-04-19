#! /usr/bin/env python
# -*- coding: utf-8 -*-
# vim:fenc=utf-8
#
"""
Create a Client for vista that can create patients and pull data
"""

import click
import requests
from abstract_client import AbstractClient


class HapiClient(AbstractClient):
    """Allow users to easy create a new patient and export all patients"""

    def __init__(self, fhir, base):
        """Constructor"""
        self.fhir = fhir
        self.base = base

    def export_patients(self):
        """Calls the FHIR API to export all patients"""
        r = requests.get(f"{self.fhir}/{self.base}/Patient", timeout=100, verify=False)
        return r

    def export_patient(self, p_id):
        """Calls the FHIR API to export all patients"""
        r = requests.get(
            f"{self.fhir}/{self.base}/Patient/{p_id}", timeout=100, verify=False
        )
        return r

    def create_patient_fromfile(self, file):
        """Create a new patient from a FHIR JSON file"""
        headers = {
            "Accept": "application/fhir+json",
            "Content-Type": "application/json",
        }
        r = requests.post(
            f"{self.fhir}/{self.base}/Patient",
            data=file.read(),
            timeout=10,
            headers=headers,
            verify=False,
        )
        return r

    def create_patient(self, data):
        """Create a new patient from a FHIR JSON file"""
        headers = {
            "Accept": "application/fhir+json",
            "Content-Type": "application/json",
        }
        r = requests.post(
            f"{self.fhir}/{self.base}/Patient",
            data=data,
            timeout=10,
            headers=headers,
            verify=False,
        )
        return r


@click.command()
@click.option("--file", type=click.File("r"))
def cli_options(file):
    """
    Extract command-line arguments to either create a new patient
    No arguments: exports all patients in a JSON form
    """
    client = HapiClient("http://localhost:8004", "fhir")
    if file is None:
        response = client.export_patients()
        if response.status_code == 200:
            print(response.json())
        else:
            print(response.status_code)
    else:
        print(client.create_patient_fromfile(file).json())


if __name__ == "__main__":
    cli_options()