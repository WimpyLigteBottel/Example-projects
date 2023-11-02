import axios from "axios";

const baseUrl = `http://localhost:8090`;

export async function fetchTasks(filterText, isCompleted) {
  if (!filterText) {
    filterText = ``;
  }

  if (!isCompleted) {
    isCompleted = ``;
  }

  let data = axios
    .get(`${baseUrl}/v1/task?message=${filterText}&completed=${isCompleted}`)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.error(error);
    });

  return data;
}

export async function updateTask(task) {
  let data = axios
    .put(`${baseUrl}/v1/task`, task)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.error(error);
    });

  return data;
}

export async function removeTask(id) {
  let data = axios
    .delete(`${baseUrl}/v1/task/${id}`)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.error(error);
    });

  return data;
}

export async function createTask() {
  let data = axios
    .get(`${baseUrl}/v1/create`)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.error(error);
    });

  return data;
}
