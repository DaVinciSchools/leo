import './SpinButton.scss';
import {useEffect, useState} from 'react';
import {doTransition} from '../../libs/transitions';
import PromiseQueue from '../../libs/PromiseQueue';

const DURATION_MS = 750;

export function SpinButton(props: {
  id: string;
  diameter: number;
  enabled: boolean;
  onClick: () => void;
}) {
  const [promises] = useState(new PromiseQueue());
  const [visible, setVisible] = useState(false);

  const [diameterFraction, setDiameterFraction] = useState(0);
  const [radians, setRadians] = useState(0);

  useEffect(() => {
    if (props.enabled) {
      promises.enqueue(() => {
        setVisible(true);
        return doTransition(
          DURATION_MS,
          {
            setFn: setDiameterFraction,
            begin: diameterFraction,
            end: 1,
          },
          {setFn: setRadians, begin: -4 * Math.PI, end: 0}
        );
      }, 'show');
    } else {
      promises.enqueue(() => {
        return doTransition(
          DURATION_MS,
          {
            setFn: setDiameterFraction,
            begin: diameterFraction,
            end: 0,
          },
          {setFn: setRadians, begin: -4 * Math.PI, end: 0}
        ).finally(() => {
          setVisible(false);
        });
      }, 'hide');
    }
  }, [props.enabled]);

  function onClick() {
    if (props.enabled) {
      props.onClick();
    }
  }

  return (
    <>
      <div
        id={props.id}
        className="spin-button"
        style={{
          left: '50%',
          top: '50%',
          width: props.diameter * diameterFraction,
          height: props.diameter * diameterFraction,
          fontSize: (props.diameter * diameterFraction) / 4,
          visibility: visible ? 'visible' : 'hidden',
          transform: `translate(-50%, -50%) rotate(${radians}rad)`,
          cursor: 'pointer',
        }}
        role="button"
        onClick={onClick}
      >
        Go!
      </div>
    </>
  );
}
