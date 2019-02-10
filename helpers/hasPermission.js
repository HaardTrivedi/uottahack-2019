import { Status } from "./Constants"

const hasPermission = status => Object.is(status, Status.GRANTED)

export default hasPermission
