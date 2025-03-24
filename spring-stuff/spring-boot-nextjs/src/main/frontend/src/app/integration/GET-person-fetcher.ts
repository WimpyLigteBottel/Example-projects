"use client";
import type { Person } from "../model/person";
import useSWR from "swr";
import { mutate } from "swr";
import { sleep } from "./Integration-util";
import { baseUrl } from "./properties";

async function getPersons() {
  return await fetch(`${baseUrl}/v1/person/`).then((res) => res.json());
}

export function usePerson() {
  const { data, isLoading, error } = useSWR(`/v1/person/`, getPersons);

  return {
    persons: data,
    isLoading,
    isError: error,
  };
}

export function invalidatePerson() {
  mutate("/v1/person/");
}
