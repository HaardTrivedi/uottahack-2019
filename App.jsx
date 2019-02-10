import React from "react"
import { Text, View, TouchableOpacity} from "react-native"
import { Camera, Permissions } from "expo"
import { CameraView } from './components/View/Camera'

function produce(f) {
  return state => {
    const copy = { ...state }
    f(copy)
    return copy
  }
}

const typeReducer = draft =>
  void (draft.type =
    draft.type === Camera.Constants.Type.back
      ? Camera.Constants.Type.front
      : Camera.Constants.Type.back)

export default class App extends React.Component {
  state = {
    hasPermission: null,
    type: Camera.Constants.Type.back
  }

  styles = {
    opacity: {
      flex: 0.1,
      alignSelf: "flex-end",
      alignItems: "center"
    },
    camera: {
      backgroundColor: "transparent",
      flexDirection: "row"
    },
    text: {
      fontSize: 18,
      marginTop: 100,
      marginBottom: 10,
      color: "white"
    }
  }

  componentDidMount() {
    this.load()
  }

  async load() {
    const permissions = await Permissions.askAsync(Permissions.CAMERA)
    this.produceState(
      draft => void (draft.hasPermission = hasPermission(permissions.status))
    )
  }

  produceState = reducer => {
    this.setState(produce(reducer))
  }

  onPress = () => this.produceState(typeReducer)

    getRef = async (ref) => {
      await Promise.resolve(this.camera = ref)
      setTimeout(() => {
        const photo = await this.camera.takePictureAsync()
        console.log(photo)
      }, 1000)
    }

  render() {
    const { hasPermission, type } = this.state
    const { camera, opacity, text } = this.styles
    return (
      <CameraView
        onFalse="No access to camera"
        hasPermission={hasPermission}
        cameraType={type}
        camera={this.getRef}
      >
        <View style={camera}>
          <TouchableOpacity style={opacity} onPress={this.onPress}>
            <Text style={text}> Flip </Text>
          </TouchableOpacity>
        </View>
      </CameraView>
    )
  }
}

