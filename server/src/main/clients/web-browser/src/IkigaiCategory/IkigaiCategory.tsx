import './IkigaiCategory.scss';
import {CSSProperties} from 'react';

enum Orientation {
  NORTH,
  NORTHWEST,
  WEST,
  SOUTHWEST,
  SOUTH,
  SOUTHEAST,
  EAST,
  NORTHEAST,
}

function getOrientationStyle(radians: number) {
  const style: Partial<CSSProperties> = {placeContent: undefined};

  // Force the angle to be between 0 and 2 * Math.PI.
  const normalizedRadians =
    ((radians % (2 * Math.PI)) + 2 * Math.PI) % (2 * Math.PI);

  // Split the angle into 16 position, 4 per quadrant.
  const segment = (normalizedRadians * 16) / (2 * Math.PI);

  // Identify the origin for each segment.
  const orientation =
    segment < 1
      ? Orientation.EAST
      : segment < 3
        ? Orientation.SOUTHEAST
        : segment < 5
          ? Orientation.SOUTH
          : segment < 7
            ? Orientation.SOUTHWEST
            : segment < 9
              ? Orientation.WEST
              : segment < 11
                ? Orientation.NORTHWEST
                : segment < 13
                  ? Orientation.NORTH
                  : segment < 15
                    ? Orientation.NORTHEAST
                    : Orientation.EAST;

  // Position vertically.
  switch (orientation) {
    case Orientation.NORTHEAST:
    case Orientation.NORTH:
    case Orientation.NORTHWEST:
      Object.assign(style, {
        justifyContent: 'flex-start',
      });
      break;
    case Orientation.SOUTHEAST:
    case Orientation.SOUTH:
    case Orientation.SOUTHWEST:
      Object.assign(style, {
        justifyContent: 'flex-end',
      });
      break;
    case Orientation.EAST:
    case Orientation.WEST:
      Object.assign(style, {
        justifyContent: 'center',
      });
      break;
  }

  // Position horizontally.
  switch (orientation) {
    case Orientation.NORTHEAST:
    case Orientation.EAST:
    case Orientation.SOUTHEAST:
      Object.assign(style, {
        alignItems: 'flex-end',
        alignContent: 'flex-end',
        textAlign: 'right',
      });
      break;
    case Orientation.SOUTHWEST:
    case Orientation.WEST:
    case Orientation.NORTHWEST:
      Object.assign(style, {
        alignItems: 'flex-start',
        alignContent: 'flex-start',
        textAlign: 'left',
      });
      break;
    case Orientation.NORTH:
    case Orientation.SOUTH:
      Object.assign(style, {
        alignItems: 'center',
        alignContent: 'center',
        textAlign: 'center',
      });
      break;
  }

  return style;
}

export function IkigaiCategory(props: {
  id: string;
  diameter: number;
  maxDiameter: number;
  backgroundAlpha: number;
  contentAlpha: number;
  radians: number;
  textRadians?: number;
  distance: number;
  categoryElementId: string;
  hue: number;
  backgroundParams?: {};
}) {
  const scale =
    props.maxDiameter !== 0 ? props.diameter / props.maxDiameter : 0;
  const diameter = props.diameter / scale;
  const edgeAt45Deg = (diameter / 2) * Math.cos(0.25 * Math.PI);

  const element = document.getElementById(props.categoryElementId);
  if (element != null) {
    Object.assign(element.style, getOrientationStyle(props.radians), {
      display: 'flex',
      flexFlow: 'column nowrap',
      position: 'absolute',
      left: '50%',
      top: '50%',
      width: diameter + 'px',
      maxWidth: diameter + 'px',
      height: diameter + 'px',
      maxHeight: diameter + 'px',
      padding: diameter / 2 - edgeAt45Deg + 'px',
      borderRadius: '50%',
      transform: `translate(calc(-50% + ${
        Math.cos(props.radians) * props.distance
      }px), calc(-50% + ${
        Math.sin(props.radians) * props.distance
      }px)) scale(${scale}, ${scale})`,
      visibility: 'visible',
      backgroundColor: `hsla(${props.hue}, 100%, 75%, ${props.backgroundAlpha})`,
      borderStyle: 'solid',
      borderWidth: '1px',
      borderColor: `hsla(${props.hue}, 100%, 80%, ${props.backgroundAlpha})`,
      opacity: props.contentAlpha,
    });
  }

  return <></>;
}
