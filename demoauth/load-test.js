import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 20,
    iterations: 1000,
};

const token = __ENV.TOKEN;

export default function () {

    const url =
        'http://host.docker.internal:8080/api/gtfs' +
        '?scopeType=REGION' +
        '&regionId=2c0d884f-fbd2-4c22-9ead-937ccb1f78c3';

    const response = http.get(url, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    check(response, {
        'status is 200': (r) => r.status === 200,
    });
}