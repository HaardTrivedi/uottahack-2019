import { NativeModules } from "react-native"

interface CatService {
  getDimension(): [number, number]
  getColor(img: string): number[]
}
