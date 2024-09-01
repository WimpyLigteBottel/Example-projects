"use client";
import type { Person } from "@/model/Person";
import useSWR from "swr";
import { mutate } from "swr";
import { sleep } from "@/integration/Integration-util";

async function getPersons() {
  await sleep(1000);
  return await fetch(`http://localhost:8080/v1/person/`).then((res) =>
    res.json(),
  );
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
