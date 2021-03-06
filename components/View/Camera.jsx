import React from "react"
import { View } from "react-native"
import { Camera } from "expo"
import { isNull, isFalse } from "../../helpers/"
const style = {
  flex: 1
}

export default function CameraView({
  text: onFalse,
  children,
  hasPermission,
  cameraType,
  camera
}) {
  return isNull(hasPermission) ? (
    <View />
  ) : isFalse(hasPermission) ? (
    <ViewText>{onFalse}</ViewText>
  ) : (
    <View style={style}>
      <Camera style={style} type={cameraType} ratio={""} ref={camera}>
        {children}
      </Camera>
    </View>
  )
}
